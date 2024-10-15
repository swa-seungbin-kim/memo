package memo.view;

import static memo.common.EventTopic.*;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.prefs.Preferences;

import memo.common.PreferenceField;
import memo.job.MemoUpdateJob;

public class Memo extends StyledText {
	
	private Long id;
	
	private Font font;
	
	private Color fontColor;
	
	private IEclipseContext context;
	

	@Inject
	private Provider<MemoUpdateJob> memoUpdateJobProvider;
	
	
	public Memo(Long id, Composite parent, IEclipseContext context) {
		
		super(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		
		this.id = id;
		this.context = context;
		
		updateFont();
		ContextInjectionFactory.inject(this, context);
		
		// CTRL + S 클릭시 메모UPDATE
		super.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				if ((e.stateMask & SWT.CTRL) != 0 && e.keyCode == 's') {
					
					MemoUpdateJob memoUpdateJob = memoUpdateJobProvider.get();
					memoUpdateJob.setParameter(id, getText());
					memoUpdateJob.schedule();
				}
			}
		});
	}
	
	
	public boolean matchesMemoId(Long id) {
		
		return Objects.equals(this.getId(), id);
	}
	
	
	public Long getId() {
		
		return id;
	}
	
	
	public void updateFont() {
		
		Preferences preferences = InstanceScope.INSTANCE.getNode("memo.preference");
		
		// 폰트설정
		String fontData = preferences.get(PreferenceField.FONT.name(), "")
				.replace("/", "");
		if (!fontData.isEmpty()) {
			Font tmp = font;
			
			font = new Font(Display.getCurrent(), new FontData(fontData));
			this.setFont(font);
			
			// 새로운 폰트 적용 후 기존 자원 제거
			if (tmp != null) {
				tmp.dispose();
			}
		}
		
		// 폰트색상 설정
		String[] fontColorRGB = preferences.get(PreferenceField.FONT_COLOR.name(), "0,0,0")
				.split(",");
		fontColor = new Color(
				Display.getCurrent(), 
				Integer.parseInt(fontColorRGB[0]),
				Integer.parseInt(fontColorRGB[1]),
				Integer.parseInt(fontColorRGB[2])
				);
		this.setForeground(fontColor);
	}
	
	
	@Inject
	@Optional
	public void applyFontPreference(@UIEventTopic(FONT_PREFERENCE_APPLY) Object object) {
		
		updateFont();
	}


	@Override
	public void dispose() {
		
		ContextInjectionFactory.uninject(this, context);
		
		if (font != null) {
			font.dispose();
		}
		fontColor.dispose();
		
		super.dispose();
	}
	
	
}
